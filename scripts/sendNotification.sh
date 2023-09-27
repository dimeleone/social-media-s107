sudo apt-get install mailutils
echo "Enviando notificação..."
echo "Pipeline executado!" | mail -s "Pipeline executado!" ${EMAIL_LIST}