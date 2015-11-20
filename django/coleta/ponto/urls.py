from rest_framework import routers
from django.conf.urls import url, include
from ponto import views

router = routers.DefaultRouter()
router.register(r'pontos', views.PontoViewSet)
router.register(r'categorias', views.CategoriaViewSet)

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
]
