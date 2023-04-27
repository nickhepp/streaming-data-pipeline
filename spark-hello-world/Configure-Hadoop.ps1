<#
.SYNOPSIS
Downloads the 'winutils' repo needed to run Hadoop on Windows, as well as configures the HADOOP_HOME environment 
variable.

.DESCRIPTION
This function checks if the winutils folder exists and if it does not, clones the winutils repository from GitHub.
It then determines the absolute path to the highest versioned subfolder within the winutils folder and sets the
HADOOP_HOME environment variable to this path. If the HADOOP_HOME environment variable does not exist, it is created.
Next, it checks for the presence of a local .gitignore file and if it does not exist, creates it. Finally, it checks
the contents of the .gitignore file and adds 'winutils' to it if it is not already present, and then runs 'git add'
on the .gitignore file.

.PARAMETER None

.EXAMPLE
Assuming you trust this script, you will need to run the script via an ELEVATED command line
and possibly bypass the default Powershell execution policy.  Here are the command line steps below:
cd <same-directory-as-this-script>
powershell.exe -ExecutionPolicy Bypass -File Configure-Hadoop.ps1

#>


$winutilsPath = "$PSScriptRoot\winutils"
if (Test-Path $winutilsPath) {
    Write-Host "Winutils folder already exists, exiting..."
    return
}

Write-Host "Cloning winutils repository..."
git clone https://github.com/steveloughran/winutils $winutilsPath

$hadoopHome = Get-ChildItem -Path $winutilsPath -Directory |
                Where-Object { $_.Name -match '\d+\.\d+\.\d+' } |
                Sort-Object -Descending |
                Select-Object -First 1 |
                Resolve-Path |
                Select-Object -ExpandProperty Path

Write-Host "HADOOP_HOME variable calculated to be: $hadoopHome"

Write-Host "Adding/updating HADOOP_HOME environment variable..."
[Environment]::SetEnvironmentVariable("HADOOP_HOME", $hadoopHome, [EnvironmentVariableTarget]::Machine)

$gitIgnorePath = "$PSScriptRoot\.gitignore"
if (-not (Test-Path $gitIgnorePath)) {
    Write-Host "Creating .gitignore file..."
    New-Item $gitIgnorePath -ItemType File
}

$gitIgnoreVal = 'winutils/'
$gitIgnoreContent = Get-Content $gitIgnorePath -Raw
if (-not ($gitIgnoreContent -match $gitIgnoreVal)) {
    Write-Host "Adding 'winutils' to .gitignore..."
    Add-Content $gitIgnorePath $gitIgnoreVal
    git add $gitIgnorePath
}

Write-Host "Don't forget to restart IntelliJ so the environment variable value is loaded in the development environment."

