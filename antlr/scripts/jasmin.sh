#!/bin/bash

#================================================================================#
# SCRIPT DE TESTE DE GERAÇÃO DE CÓDIGO AUTOMATIZADO                              #
# (Versão que lida com casos que exigem input do usuário)                        #
#================================================================================#

# --- Configuração Dinâmica ---

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# <<< MODIFICAÇÃO >>>
# Ajustei o diretório de volta para ../../Lang para encontrar todos os arquivos
# que você listou na sua saída de erro (simple/, function/, etc.)
DIRETORIO_TESTES="${SCRIPT_DIR}/../../Lang/types"
ARQUIVO_LOG="${SCRIPT_DIR}/resultados_geracao_codigo.log"
MAKE_DIR="${SCRIPT_DIR}/.."

# --- Inicialização ---

echo "--- Iniciando Testes de Geração de Código ---"
echo "Diretório de busca dos testes: ${DIRETORIO_TESTES}"
echo "Os resultados serão salvos em: ${ARQUIVO_LOG}"

> "$ARQUIVO_LOG"
echo "Arquivo de log limpo e pronto para novos registros."
echo ""

# --- Loop de Testes ---

find "$DIRETORIO_TESTES" -type f -name "*.lan" | while read arquivo; do
    nome_relativo=$(realpath --relative-to="${DIRETORIO_TESTES}" "$arquivo")
    echo -n "Testando: ${nome_relativo}..."

    # <<< INÍCIO DA MODIFICAÇÃO PRINCIPAL >>>

    # Variável para guardar o comando a ser executado
    comando_executar=""
    input_fornecido=""

    # Verifica se o arquivo atual é um dos casos especiais
    if [[ "$nome_relativo" == "simple/ifchain.lan" ]]; then
        input_fornecido="5" # Fornece o número 5 como entrada
        comando_executar="echo '$input_fornecido' | make -C '${MAKE_DIR}' run-gen '${arquivo}' 2>&1"
        echo -n " (com input '${input_fornecido}')..."

    elif [[ "$nome_relativo" == "simple/read.lan" ]]; then # Adapte este caminho se 'read.lan' estiver em outra pasta
        input_fornecido="42" # Fornece o número 42 como entrada
        comando_executar="echo '$input_fornecido' | make -C '${MAKE_DIR}' run-gen '${arquivo}' 2>&1"
        echo -n " (com input '${input_fornecido}')..."

    else
        # Caso padrão: executa sem fornecer nenhuma entrada
        comando_executar="make -C '${MAKE_DIR}' run-gen '${arquivo}' 2>&1"
    fi

    # Executa o comando definido (com ou sem input)
    # 'eval' é usado aqui para garantir que o pipe '|' seja interpretado corretamente
    output=$(eval $comando_executar)
    exit_code=$?

    # <<< FIM DA MODIFICAÇÃO PRINCIPAL >>>


    # A lógica de verificação do resultado continua a mesma
    if [ $exit_code -eq 0 ]; then
        echo "${nome_relativo}: OK" >> "$ARQUIVO_LOG"
        echo " [OK]"
    else
        echo "${nome_relativo}: FALHA" >> "$ARQUIVO_LOG"
        echo " [FALHA]"
    fi
done

echo ""
echo "--- Testes de Geração de Código Concluídos ---"
echo "Verifique o arquivo '${ARQUIVO_LOG}' para ver todos os detalhes."