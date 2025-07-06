$entradaDir = "..\Lang\sintaxe\certo"
$dotDir = "dotFiles"
$pngDir = "dotFiles\png"
$errorDir = "dotFiles\errors"

# Cria pastas, se necessário
New-Item -ItemType Directory -Force -Path $dotDir, $pngDir, $errorDir | Out-Null

# Compilação
javac -cp ".;antlr-4.8-complete.jar" parser\*.java ast\*.java Main.java

# Loop por arquivos .lan
Get-ChildItem -Path $entradaDir -Filter *.lan | ForEach-Object {
    $arquivo = $_.FullName
    $base = $_.BaseName
    $dotFile = "$dotDir\$base.dot"
    $dotErrorFile = "$dotDir\${base}_error.dot"
    $pngFile = "$pngDir\$base.png"
    $errorFile = "$errorDir\$base.error.txt"

    Write-Output "---------------------------"
    Write-Output "Executando: $arquivo"

    # Limpa arquivos anteriores
    Remove-Item -Force -ErrorAction Ignore $dotFile, $dotErrorFile, $pngFile, $errorFile

    # Executa o Main
    java -cp ".;antlr-4.8-complete.jar" Main "$arquivo" 2> $errorFile

    if (-Not (Test-Path $dotFile)) {
        Write-Output "❌ AST não gerada para $base. Criando .dot com erro."
        "// erro na geração da AST" | Out-File -Encoding utf8 $dotErrorFile
        return
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
    if ((Get-Content $errorFile).Length -eq 0) {
        Remove-Item $errorFile
    }
}
