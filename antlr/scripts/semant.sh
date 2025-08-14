#!/bin/bash

#================================================================================#
# SCRIPT DE TESTE SEMÂNTICO AUTOMATIZADO                                         #
#                                                                                #
# Este script executa a análise semântica para todos os arquivos .lan            #
# encontrados no diretório Lang e suas subpastas.                                #
#================================================================================#

# --- Configuração Dinâmica ---

# Encontra o diretório onde o próprio script está localizado.
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# Define os outros caminhos com base na localização do script.
# ${SCRIPT_DIR} -> /.../trabalhoCompiladores/antlr/scripts
DIRETORIO_TESTES="${SCRIPT_DIR}/../../Lang/types"                  # Alvo: A pasta 'Lang' inteira.
ARQUIVO_LOG="${SCRIPT_DIR}/resultados_semanticos.log"      # Novo arquivo de log para os resultados semânticos.
MAKE_DIR="${SCRIPT_DIR}/.."                                  # O Makefile está um nível acima (na pasta antlr).

# --- Inicialização ---

echo "--- Iniciando Testes Semânticos ---"
echo "Diretório de busca dos testes: ${DIRETORIO_TESTES}"
echo "Os resultados serão salvos em: ${ARQUIVO_LOG}"

# Limpa o arquivo de log anterior para esta execução.
> "$ARQUIVO_LOG"
echo "Arquivo de log limpo e pronto para novos registros."
echo ""

# --- Loop de Testes ---

# O comando 'find' irá percorrer todas as subpastas dentro de DIRETORIO_TESTES.
find "$DIRETORIO_TESTES" -type f -name "*.lan" | while read arquivo; do
    # 'realpath --relative-to' mostra um caminho mais informativo no log e no terminal.
    nome_relativo=$(realpath --relative-to="${DIRETORIO_TESTES}" "$arquivo")
    echo -n "Testando: ${nome_relativo}..."

    # Executa o comando make para a análise SEMÂNTICA.
    # A única grande mudança está aqui: 'run-semant' em vez de 'run-syn'.
    # A saída de erro (2>/dev/null) continua sendo descartada para focar na captura do resultado.
    resultado=$(make -C "${MAKE_DIR}" run-semant "$arquivo" 2>/dev/null | grep -o 'accept\|reject')

    # A lógica para registrar o resultado é a mesma do script anterior.
    if [[ -n "$resultado" ]]; then
        echo "${nome_relativo}: ${resultado}" >> "$ARQUIVO_LOG"
        echo " [OK]"
    else
        # Se a análise semântica produzir um erro específico (diferente de 'reject'),
        # ele será capturado aqui como FALHA.
        echo "${nome_relativo}: FALHA (nenhum 'accept' ou 'reject' encontrado)" >> "$ARQUIVO_LOG"
        echo " [FALHA]"
    fi
done

echo ""
echo "--- Testes Semânticos Concluídos ---"
echo "Verifique o arquivo '${ARQUIVO_LOG}' para ver todos os detalhes."