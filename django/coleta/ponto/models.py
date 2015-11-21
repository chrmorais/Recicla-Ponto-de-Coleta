from django.db import models
from rest_framework import serializers


class Categoria(models.Model):
    nome = models.CharField(max_length=250)

    def __str__(self):
        return self.nome


class Ponto(models.Model):
    nome = models.CharField(max_length=250, help_text='Este é o nome que aparecerá no mapa')
    latitude = models.FloatField('Latitude', help_text='Por favor, utilize a latitude do ponto de coleta')
    longitude = models.FloatField('Longitude', help_text='Por favor, utilize a longitude do ponto de coleta')
    categorias = models.ManyToManyField(Categoria)
    endereco = models.CharField(max_length=500, help_text='Coloque o endereço para ajudar os usuários a encontrar o local')

    def __str__(self):
        return self.nome


class CategoriaSerializer(serializers.HyperlinkedModelSerializer):

    class Meta:
        model = Categoria
        fields = ('nome', )


class PontoSerializer(serializers.HyperlinkedModelSerializer):

    class Meta:
        model = Ponto
        fields = ('id', 'nome', 'latitude', 'longitude', 'categorias', 'endereco', )
