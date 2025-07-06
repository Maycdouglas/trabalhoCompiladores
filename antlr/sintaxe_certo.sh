#!/bin/bash

# --- Configuração ---
# Diretório com os arquivos de código-fonte .lan
ENTRADA_DIR="../Lang/sintaxe/certo"

# Diretórios de saída
DOT_DIR="dotFiles"
PNG_DIR="$DOT_DIR/png"
ERROR_DIR="$DOT_DIR/errors"

# --- Execução ---

echo "1. Criando todos os diretórios de saída necessários..."
# O comando 'mkdir -p' cria a estrutura completa de diretórios
# e não falha se eles já existirem.
mkdir -p "$PNG_DIR" "$ERROR_DIR"

echo "2. Limpando e compilando o projeto via Makefile..."
# Usa o Makefile para garantir que o ambiente esteja limpo e compilado
make clean
make compile

# Verifica se a compilação foi bem-sucedida antes de continuar
if [ $? -ne 0 ]; then
    echo "❌ Erro fatal na compilação. Abortando o script."
    exit 1
fi

echo "3. Iniciando o processamento em lote dos arquivos .lan..."
# Itera sobre os arquivos e usa o alvo 'ast' para processar cada um
for arquivo in "$ENTRADA_DIR"/*.lan; do
    echo "---------------------------"
    echo "Processando: $arquivo"
    
    base=$(basename "$arquivo" .lan)
    error_file="$ERROR_DIR/$base.error.txt"
    
    # Executa o alvo 'ast' do Makefile para o arquivo atual.
    # Este alvo já sabe como gerar o .dot e o .png.
    # Capturamos a saída de erro (stderr) para nosso log.
    if make ast "$arquivo" > /dev/null 2> "$error_file"; then
        echo "✅ Sucesso: AST e PNG gerados para '$base'."
    else
        echo "❌ Erro ao processar '$base'. Verifique o log: $error_file"
    fi

    # Remove o arquivo de log de erro se ele estiver vazio
    [ -s "$error_file" ] || rm -f "$error_file"
done

echo "---------------------------"
echo "✅ Processamento de todos os arquivos concluído."