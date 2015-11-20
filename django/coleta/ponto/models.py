from django.db import models


class Categoria(models.Model):
    nome = models.CharField(max_length=250)


class Ponto(models.Model):
    nome = models.CharField(max_length=250)
    latitude = models.FloatField('Latitude', help_text='Por favor, utilize a latitude do ponto de coleta')
    longitude = models.FloatField('Longitude', help_text='Por favor, utilize a longitude do ponto de coleta')
    categorias = models.ManyToManyField(Categoria)
