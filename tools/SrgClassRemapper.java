import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

public final class SrgClassRemapper {
    private SrgClassRemapper() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            throw new IllegalArgumentException("usage: SrgClassRemapper <forge.srg> <inputRoot> <outputRoot>");
        }

        Path srgPath = Paths.get(args[0]).toAbsolutePath().normalize();
        Path inputRoot = Paths.get(args[1]).toAbsolutePath().normalize();
        Path outputRoot = Paths.get(args[2]).toAbsolutePath().normalize();

        MappingTables mappings = MappingTables.load(srgPath);
        if (Files.exists(outputRoot)) {
            deleteRecursively(outputRoot);
        }
        Files.createDirectories(outputRoot);

        try (Stream<Path> stream = Files.walk(inputRoot)) {
            stream
                .filter(Files::isRegularFile)
                .forEach(path -> remapOne(path, inputRoot, outputRoot, mappings));
        }
    }

    private static void remapOne(Path inputPath, Path inputRoot, Path outputRoot, MappingTables mappings) {
        Path relativePath = inputRoot.relativize(inputPath);
        Path outputPath = outputRoot.resolve(relativePath);
        try {
            Files.createDirectories(outputPath.getParent());
            if (!inputPath.toString().endsWith(".class")) {
                Files.copy(inputPath, outputPath);
                return;
            }

            byte[] transformed = remapClass(inputPath, mappings);
            Files.write(outputPath, transformed);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to remap " + inputPath, exception);
        }
    }

    private static byte[] remapClass(Path inputPath, MappingTables mappings) throws IOException {
        try (InputStream inputStream = Files.newInputStream(inputPath)) {
            ClassReader reader = new ClassReader(inputStream);
            ClassWriter writer = new ClassWriter(0);
            ClassRemapper remapper = new ClassRemapper(writer, mappings.createRemapper());
            reader.accept(remapper, 0);
            return writer.toByteArray();
        }
    }

    private static void deleteRecursively(Path root) throws IOException {
        try (Stream<Path> stream = Files.walk(root)) {
            stream
                .sorted((left, right) -> right.getNameCount() - left.getNameCount())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException exception) {
                        throw new IllegalStateException("Failed to delete " + path, exception);
                    }
                });
        }
    }

    private static final class MappingTables {
        private final Map<String, String> classMappings = new HashMap<>();
        private final Map<String, String> fieldMappings = new HashMap<>();
        private final Map<String, String> methodMappings = new HashMap<>();

        private static MappingTables load(Path srgPath) throws IOException {
            MappingTables tables = new MappingTables();
            List<String> lines = Files.readAllLines(srgPath);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length < 3) {
                    continue;
                }

                String type = parts[0];
                if ("CL:".equals(type) && parts.length >= 3) {
                    tables.classMappings.put(parts[1], parts[2]);
                } else if ("FD:".equals(type) && parts.length >= 3) {
                    String[] oldField = splitMember(parts[1]);
                    String[] newField = splitMember(parts[2]);
                    tables.fieldMappings.put(oldField[0] + "/" + oldField[1], newField[1]);
                } else if ("MD:".equals(type) && parts.length >= 5) {
                    String[] oldMethod = splitMember(parts[1]);
                    String[] newMethod = splitMember(parts[3]);
                    tables.methodMappings.put(oldMethod[0] + "/" + oldMethod[1] + " " + parts[2], newMethod[1]);
                }
            }
            return tables;
        }

        private Remapper createRemapper() {
            return new Remapper() {
                @Override
                public String map(String internalName) {
                    String mapped = classMappings.get(internalName);
                    return mapped == null ? internalName : mapped;
                }

                @Override
                public String mapFieldName(String owner, String name, String descriptor) {
                    String mapped = fieldMappings.get(owner + "/" + name);
                    return mapped == null ? name : mapped;
                }

                @Override
                public String mapMethodName(String owner, String name, String descriptor) {
                    String mapped = methodMappings.get(owner + "/" + name + " " + descriptor);
                    return mapped == null ? name : mapped;
                }
            };
        }

        private static String[] splitMember(String value) {
            int index = value.lastIndexOf('/');
            if (index < 0 || index == value.length() - 1) {
                throw new IllegalArgumentException("Invalid member mapping: " + value);
            }
            return new String[] { value.substring(0, index), value.substring(index + 1) };
        }
    }
}
