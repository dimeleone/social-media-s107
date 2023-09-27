sudo apt-get install mailutils
echo "Enviando notificação..."
echo "Pipeline executado!" | mail -s "a subject" ${EMAIL_LIST}