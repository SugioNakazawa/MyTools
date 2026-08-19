#!/bin/bash
#
# Git のコミット履歴から作成者ごとの追加行数・削除行数を集計し、
# バイナリなど行数を取得できない変更を除外して、追加行数の多い順に表示する。
echo "========================================="
echo " ユーザーごとのコード変更量（累計）"
echo "========================================="
echo -e "追加行数\t削除行数\tユーザー名"
echo "----------------------------------------="

# git log から作成者名を取得し、重複を排除してループ
git log --format='%aN' | sort -u | while read -r author; do
    # 各ユーザーの「追加行」と「削除行」の合計を抽出して計算
    stats=$(git log --author="$author" --numstat --pretty=tformat: | \
            awk 'NF==3 && $1!~/^-$/ && $2!~/^-$/ { add+=$1; del+=$2 } END { print add "\t" del }')
    
    # 結果が空（コミットはあるがバイナリのみ等の場合）は 0 0 にする
    if [ -z "$stats" ]; then
        stats="0\t0"
    fi
    
    echo -e "${stats}\t${author}"
done | sort -nr -k1 # 追加行数が多い順にソート

