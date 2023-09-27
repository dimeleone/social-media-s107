sudo apt-get install mailutils
echo "Enviando notificação para ${EMAIL_LIST}"
echo ""Pipeline executado!" | mail -s "a subject" ${EMAIL_LIST}