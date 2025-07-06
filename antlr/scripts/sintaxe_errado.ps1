# Garante que o script sempre execute a partir do diretório pai (antlr)
Set-Location -Path (Get-Item -Path $PSScriptRoot).Parent.FullName

# --- Caminhos alterados para o contexto de "errado" ---
$entradaDir = "..\Lang\sintaxe\errado"
$dotDir = "dotFiles\errados"
$pngDir = "$dotDir\png"
$errorDir = "$dotDir\errors"

# Cria pastas, se necessário
New-Item -ItemType Directory -Force -Path $dotDir, $pngDir, $errorDir | Out-Null

# Compilação
Write-Output "Compilando fontes Java..."
javac -cp ".;antlr-4.8-complete.jar" parser\*.java ast\*.java error\*.java Main.java

# Loop por arquivos .lan
Get-ChildItem -Path $entradaDir -Filter *.lan | ForEach-Object {
    $arquivo = $_.FullName
    $base = $_.BaseName
    $dotFile = Join-Path -Path $dotDir -ChildPath "$base.dot"
    $dotErrorFile = Join-Path -Path $dotDir -ChildPath "${base}_error.dot"
    $pngFile = Join-Path -Path $pngDir -ChildPath "$base.png"
    $errorFile = Join-Path -Path $errorDir -ChildPath "$base.error.txt"

    Write-Output "---------------------------"
    Write-Output "Executando: $arquivo"

    # Limpa arquivos anteriores
    Remove-Item -Force -ErrorAction Ignore $dotFile, $dotErrorFile, $pngFile, $errorFile

    # Executa o Main, passando o diretório de saída ($dotDir) como segundo argumento
    # Usando Start-Process para melhor controle de redirecionamento
    $process = Start-Process java -ArgumentList "-cp", ".;antlr-4.8-complete.jar", "Main", "`"$arquivo`"", "`"$dotDir`"" -NoNewWindow -PassThru -RedirectStandardError $errorFile
    $process.WaitForExit()

    if (-Not (Test-Path $dotFile)) {
        Write-Output "❌ AST não gerada para $base. Verifique o arquivo de erro em: $errorFile"
        "// erro na geração da AST" | Out-File -Encoding utf8 $dotErrorFile
        continue
    }

    Write-Output "✅ AST gerada: $dotFile"

    # Gera imagem com dot
    try {
        dot -Tpng $dotFile -o $pngFile 2>> $errorFile
        Write-Output "✅ PNG gerado: $pngFile"
    } catch {
        Write-Output "❌ Erro ao gerar PNG. Renomeando .dot"
        Move-Item -Force $dotFile $dotErrorFile
    }

    # Remove arquivo de erro se estiver vazio
    if ((Test-Path $errorFile) -and (Get-Content $errorFile).Length -eq 0) {
        Remove-Item $errorFile
    }
}