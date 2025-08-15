#!/bin/bash

#================================================================================#
# SCRIPT DE TESTE DE GERAÇÃO DE CÓDIGO AUTOMATIZADO                              #
#                                                                                #
# Este script executa o gerador de código para todos os arquivos .lan            #
# e verifica se o processo foi concluído com sucesso (código de saída 0).        #
#================================================================================#

# --- Configuração Dinâmica ---

# Encontra o diretório onde o próprio script está localizado.
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# Define os outros caminhos com base na localização do script.
DIRETORIO_TESTES="${SCRIPT_DIR}/../../Lang/types"                  # Alvo: A pasta 'Lang' inteira.
ARQUIVO_LOG="${SCRIPT_DIR}/resultados_geracao_codigo.log"  # Novo arquivo de log para esta etapa.
MAKE_DIR="${SCRIPT_DIR}/.."                                  # O Makefile está um nível acima (na pasta antlr).

# --- Inicialização ---

echo "--- Iniciando Testes de Geração de Código ---"
echo "Diretório de busca dos testes: ${DIRETORIO_TESTES}"
echo "Os resultados serão salvos em: ${ARQUIVO_LOG}"

# Limpa o arquivo de log anterior para esta execução.
> "$ARQUIVO_LOG"
echo "Arquivo de log limpo e pronto para novos registros."
echo ""

# --- Loop de Testes ---

find "$DIRETORIO_TESTES" -type f -name "*.lan" | while read arquivo; do
    nome_relativo=$(realpath --relative-to="${DIRETORIO_TESTES}" "$arquivo")
    echo -n "Testando: ${nome_relativo}..."

    # Executa o comando make para a GERAÇÃO DE CÓDIGO.
    # A saída (stdout e stderr) é capturada na variável 'output'.
    output=$(make -C "${MAKE_DIR}" run-gen "$arquivo" 2>&1)
    
    # Imediatamente após, captura o código de saída do comando anterior.
    exit_code=$?

    # Verifica se o código de saída foi 0 (sucesso).
    if [ $exit_code -eq 0 ]; then
        echo "${nome_relativo}: OK" >> "$ARQUIVO_LOG"
        echo " [OK]"
    else
        # Se o código de saída for diferente de 0, o teste falhou.
        echo "${nome_relativo}: FALHA" >> "$ARQUIVO_LOG"
        # Adiciona a saída do compilador no log para facilitar a depuração.
        echo "===================== SAÍDA DO ERRO =====================" >> "$ARQUIVO_LOG"
        echo "$output" >> "$ARQUIVO_LOG"
        echo "=========================================================" >> "$ARQUIVO_LOG"
        echo "" >> "$ARQUIVO_LOG"
        echo " [FALHA]"
    fi
done

echo ""
echo "--- Testes de Geração de Código Concluídos ---"
echo "Verifique o arquivo '${ARQUIVO_LOG}' para ver todos os detalhes."