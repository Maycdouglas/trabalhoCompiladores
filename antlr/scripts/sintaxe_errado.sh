#!/bin/bash
# Garante que o script execute a partir do diretório 'antlr'
cd "$(dirname "$0")/.."

ENTRADA_DIR="../Lang/sintaxe/errado"
DOT_DIR="dotFiles/errados"
PNG_DIR="$DOT_DIR/png"
ERROR_DIR="$DOT_DIR/errors"

# O script continua responsável por criar as pastas
mkdir -p "$DOT_DIR" "$PNG_DIR" "$ERROR_DIR"

CP=".:antlr-4.8-complete.jar"
javac -cp "$CP" parser/*.java ast/*.java error/*.java Main.java

for arquivo in "$ENTRADA_DIR"/*.lan; do
    echo "---------------------------"
    echo "Executando: $arquivo"

    base=$(basename "$arquivo" .lan)
    dot_file="$DOT_DIR/$base.dot"
    dot_file_error="$DOT_DIR/${base}_error.dot"
    png_file="$PNG_DIR/$base.png"
    error_file="$ERROR_DIR/$base.error.txt"

    rm -f "$dot_file" "$dot_file_error" "$png_file" "$error_file"

    # --- LINHA MODIFICADA ---
    # Passa o diretório de saída ($DOT_DIR) como segundo argumento para o Main
    java -cp "$CP" Main "$arquivo" "$DOT_DIR" > /dev/null 2> "$error_file"

    if [ ! -f "$dot_file" ]; then
        echo "❌ AST não gerada para $base. Criando .dot com erro."
        echo "// erro na geração da AST" > "$dot_file_error"
        continue
    fi

    echo "✅ AST gerada: $dot_file"

    if dot -Tpng "$dot_file" -o "$png_file" 2>> "$error_file"; then
        echo "✅ PNG gerado: $png_file"
    else
        echo "❌ Erro ao gerar PNG. Renomeando .dot"
        mv "$dot_file" "$dot_file_error"
        rm -f "$png_file"
    fi

    [ -s "$error_file" ] || rm -f "$error_file"
done