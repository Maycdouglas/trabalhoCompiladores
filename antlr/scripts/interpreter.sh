#!/bin/bash

#================================================================================#
# SCRIPT DE TESTE DO INTERPRETADOR AUTOMATIZADO                                  #
#                                                                                #
# Este script executa o interpretador para todos os arquivos .lan                #
# e verifica se o processo foi concluído com sucesso (código de saída 0).        #
#================================================================================#

# --- Configuração Dinâmica ---

# Encontra o diretório onde o próprio script está localizado.
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# Define os outros caminhos com base na localização do script.
DIRETORIO_TESTES="${SCRIPT_DIR}/../../Lang"
ARQUIVO_LOG="${SCRIPT_DIR}/resultados_interpretador.log"  # Novo arquivo de log para esta etapa.
MAKE_DIR="${SCRIPT_DIR}/.."                                  # O Makefile está um nível acima (na pasta antlr).

# --- Inicialização ---

echo "--- Iniciando Testes do Interpretador ---"
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

    # Define o comando a ser executado, tratando os casos especiais
    comando_executar=""
    input_fornecido=""

    if [[ "$nome_relativo" == "simple/ifchain.lan" ]]; then
        input_fornecido="5"
        # Alvo do make alterado para 'run-interp'
        comando_executar="echo '$input_fornecido' | make -C '${MAKE_DIR}' run-interp '${arquivo}' 2>&1"
        echo -n " (com input '${input_fornecido}')..."

    elif [[ "$nome_relativo" == "simple/read.lan" ]]; then # Adapte o caminho se necessário
        input_fornecido="42"
        # Alvo do make alterado para 'run-interp'
        comando_executar="echo '$input_fornecido' | make -C '${MAKE_DIR}' run-interp '${arquivo}' 2>&1"
        echo -n " (com input '${input_fornecido}')..."

    else
        # Caso padrão com o alvo 'run-interp'
        comando_executar="make -C '${MAKE_DIR}' run-interp '${arquivo}' 2>&1"
    fi

    # Executa o comando e captura o código de saída
    output=$(eval $comando_executar)
    exit_code=$?

    # Verifica o resultado e gera o log simples
    if [ $exit_code -eq 0 ]; then
        echo "${nome_relativo}: OK" >> "$ARQUIVO_LOG"
        echo " [OK]"
    else
        echo "${nome_relativo}: FALHA" >> "$ARQUIVO_LOG"
        echo " [FALHA]"
    fi
done

echo ""
echo "--- Testes do Interpretador Concluídos ---"
echo "Verifique o arquivo '${ARQUIVO_LOG}' para ver todos os detalhes."