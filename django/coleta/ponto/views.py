from rest_framework import viewsets
from ponto.models import Ponto, PontoSerializer, Categoria, CategoriaSerializer


class PontoViewSet(viewsets.ModelViewSet):

    queryset = Ponto.objects.all().order_by('nome')
    serializer_class = PontoSerializer
    filter_fields = ('categorias', )


class CategoriaViewSet(viewsets.ModelViewSet):

    queryset = Categoria.objects.all().order_by('nome')
    serializer_class = CategoriaSerializer
