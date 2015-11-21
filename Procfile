web: python django/coleta/manage.py collectstatic --noinput; bin/gunicorn_django --workers=4 --bind=0.0.0.0:$PORT django/coleta/settings.py
web: sh -c 'cd ./django/coleta && exec gunicorn coleta.wsgi --log-file -'

