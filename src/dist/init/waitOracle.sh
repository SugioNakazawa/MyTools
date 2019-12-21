while :; do
    echo "exit"|sqlplus -L -S system/password@mydb &> /dev/null
    [[ $? -eq 0 ]] && break
    sleep 1
done

