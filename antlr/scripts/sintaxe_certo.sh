#!/bin/bash
# Garante que a execução ocorra a partir da pasta 'antlr'
cd "$(dirname "$0")/.."

ENTRADA_DIR="../Lang/sintaxe/certo"
DOT_DIR="dotFiles/certos"
PNG_DIR="$DOT_DIR/png"
ERROR_DIR="$DOT_DIR/errors"

# O script cria todos os diretórios necessários
mkdir -p "$DOT_DIR" "$PNG_DIR" "$ERROR_DIR"

CP=".:antlr-4.8-complete.jar"
javac -cp "$CP" parser/*.java ast/*.java error/*.java Main.java 

for arquivo in "$ENTRADA_DIR"/*.lan; do
    echo "---------------------------"
    echo "Executando: $arquivo"

    base=$(basename "$arquivo" .lan)
    # Define as variáveis de caminho completo para uso DENTRO do script
    dot_file_path="$DOT_DIR/$base.dot"
    png_file_path="$PNG_DIR/$base.png"
    error_file_path="$ERROR_DIR/$base.error.txt"

    rm -f "$dot_file_path" "$PNG_DIR/${base}_error.dot" "$png_file_path" "$error_file_path"

    # --- LINHA CRÍTICA ---
    # Passa o caminho de entrada e APENAS O DIRETÓRIO de saída para o Main
    java -cp "$CP" Main "$arquivo" "$DOT_DIR" > /dev/null 2> "$error_file_path"

    # O resto do script continua usando as variáveis de caminho completo
    if [ ! -f "$dot_file_path" ]; then
        echo "❌ AST não gerada para $base. Criando .dot com erro."
        echo "// erro na geração da AST" > "$PNG_DIR/${base}_error.dot"
        continue
    fi

    echo "✅ AST gerada: $dot_file_path"

    if dot -Tpng "$dot_file_path" -o "$png_file_path" 2>> "$error_file_path"; then
        echo "✅ PNG gerado: $png_file_path"
    else
        echo "❌ Erro ao gerar PNG. Renomeando .dot"
        mv "$dot_file_path" "$PNG_DIR/${base}_error.dot"
        rm -f "$png_file_path"
    fi

    [ -s "$error_file_path" ] || rm -f "$error_file_path"
done