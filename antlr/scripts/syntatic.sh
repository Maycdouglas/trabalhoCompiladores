#!/bin/bash

#================================================================================#
# SCRIPT DE TESTE SINTÁTICO AUTOMATIZADO (VERSÃO ROBUSTA)                        #
#================================================================================#

# --- Configuração Dinâmica ---

# Encontra o diretório onde o próprio script está localizado.
# Isto torna o script executável de qualquer lugar.
SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# Define os outros caminhos com base na localização do script.
# ${SCRIPT_DIR} -> /.../trabalhoCompiladores/antlr/scripts
#
# Alvo: A pasta 'Lang' inteira, que está dois níveis acima da pasta 'scripts'.
DIRETORIO_TESTES="${SCRIPT_DIR}/../../Lang/sintaxe"
ARQUIVO_LOG="${SCRIPT_DIR}/resultados_sintaticos.log"  # Salva o log na mesma pasta do script
MAKE_DIR="${SCRIPT_DIR}/.." # O Makefile está um nível acima (na pasta antlr)

# --- Inicialização ---

echo "--- Iniciando Testes Sintáticos ---"
echo "Diretório de busca dos testes: ${DIRETORIO_TESTES}"
echo "Os resultados serão salvos em: ${ARQUIVO_LOG}"

> "$ARQUIVO_LOG"
echo "Arquivo de log limpo e pronto para novos registros."
echo ""

# --- Loop de Testes (o restante do script continua igual) ---

# O comando 'find' irá percorrer todas as subpastas dentro de DIRETORIO_TESTES
find "$DIRETORIO_TESTES" -type f -name "*.lan" | while read arquivo; do
    # Usamos 'realpath --relative-to' para mostrar um caminho mais informativo no log
    nome_relativo=$(realpath --relative-to="${DIRETORIO_TESTES}" "$arquivo")
    echo -n "Testando: ${nome_relativo}..."
    resultado=$(make -C "${MAKE_DIR}" run-syn "$arquivo" 2>/dev/null | grep -o 'accept\|reject')

    if [[ -n "$resultado" ]]; then
        echo "${nome_relativo}: ${resultado}" >> "$ARQUIVO_LOG"
        echo " [OK]"
    else
        echo "${nome_relativo}: FALHA (nenhum 'accept' ou 'reject' encontrado)" >> "$ARQUIVO_LOG"
        echo " [FALHA]"
    fi
done

echo ""
echo "--- Testes Concluídos ---"
echo "Verifique o arquivo '${ARQUIVO_LOG}' para ver todos os detalhes."