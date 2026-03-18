$ErrorActionPreference = 'Stop'

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
$repoRoot = Split-Path -Parent $PSScriptRoot
$modId = 'redstone_chemical_elements'
$resourceRoot = Join-Path $repoRoot 'src\main\resources\assets\redstone_chemical_elements'
$catalogPath = Join-Path $repoRoot 'src\main\java\com\chinaex123\redstone_chemical_elements\register\ElementCatalog.java'

function Write-Utf8File {
    param(
        [string]$Path,
        [string]$Content
    )

    $directory = Split-Path -Parent $Path
    if (-not (Test-Path $directory)) {
        New-Item -ItemType Directory -Force -Path $directory | Out-Null
    }

    [System.IO.File]::WriteAllText($Path, $Content, $utf8NoBom)
}

function Get-Elements {
    $lines = Get-Content $catalogPath
    $elements = New-Object System.Collections.Generic.List[string]
    $inElements = $false

    foreach ($line in $lines) {
        if ($line -match 'public static final String\[\] ELEMENTS') {
            $inElements = $true
            continue
        }

        if ($inElements -and $line -match '^\s+"([^"]+)"') {
            $elements.Add($matches[1])
            continue
        }

        if ($inElements -and $line -match '^\s*};') {
            break
        }
    }

    return $elements
}

function New-BlockStateJson {
    param([string]$Element, [string]$Name)

    return @"
{
  "variants": {
    "normal": {
      "model": "${modId}:$Element/$Name"
    }
  }
}
"@
}

function New-BlockModelJson {
    param([string]$Element, [string]$Name)

    return @"
{
  "parent": "block/cube_all",
  "textures": {
    "all": "${modId}:blocks/$Element/$Name"
  }
}
"@
}

function New-ItemModelJson {
    param([string]$Element, [string]$Name)

    return @"
{
  "parent": "${modId}:block/$Element/$Name"
}
"@
}

function Get-BlockResourceNames {
    param([string]$Element)

    return @(
        "${element}_block",
        "raw_${element}_block",
        "${element}_ore"
    )
}

function Remove-UnusedFiles {
    param([string]$BasePath)

    if (-not (Test-Path $BasePath)) {
        return
    }

    Get-ChildItem -Recurse -File $BasePath | Where-Object {
        $_.Name -notmatch '(^raw_.*_block\.(json|png)$)|(^.*_block\.(json|png)$)|(^.*_ore\.(json|png)$)'
    } | Remove-Item -Force

    Get-ChildItem -Recurse -Directory $BasePath |
        Sort-Object FullName -Descending |
        Where-Object { -not (Get-ChildItem -Force $_.FullName) } |
        Remove-Item -Force
}

function Copy-TexturesToLegacyFolder {
    param(
        [string]$SourcePath,
        [string]$TargetPath
    )

    if (-not (Test-Path $SourcePath)) {
        return
    }

    Get-ChildItem -Recurse -File $SourcePath | ForEach-Object {
        $relativePath = $_.FullName.Substring($SourcePath.Length + 1)
        $destination = Join-Path $TargetPath $relativePath
        $directory = Split-Path -Parent $destination
        if (-not (Test-Path $directory)) {
            New-Item -ItemType Directory -Force -Path $directory | Out-Null
        }
        Copy-Item $_.FullName $destination -Force
    }
}

$elements = Get-Elements

foreach ($element in $elements) {
    foreach ($name in (Get-BlockResourceNames -Element $element)) {
        Write-Utf8File -Path (Join-Path $resourceRoot "blockstates\$element\$name.json") -Content (New-BlockStateJson -Element $element -Name $name)
        Write-Utf8File -Path (Join-Path $resourceRoot "models\block\$element\$name.json") -Content (New-BlockModelJson -Element $element -Name $name)
        Write-Utf8File -Path (Join-Path $resourceRoot "models\item\$element\$name.json") -Content (New-ItemModelJson -Element $element -Name $name)
    }
}

foreach ($path in @(
    (Join-Path $resourceRoot 'blockstates'),
    (Join-Path $resourceRoot 'models\block'),
    (Join-Path $resourceRoot 'textures\block')
)) {
    Remove-UnusedFiles -BasePath $path
}

Get-ChildItem -File (Join-Path $resourceRoot 'lang') -Filter '*.json' | Remove-Item -Force
Copy-TexturesToLegacyFolder -SourcePath (Join-Path $resourceRoot 'textures\block') -TargetPath (Join-Path $resourceRoot 'textures\blocks')

Write-Output ("ELEMENTS={0}" -f $elements.Count)
Write-Output 'RESOURCE_REBUILD_DONE'
