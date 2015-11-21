from django.contrib import admin
from .models import Categoria
from .models import Ponto


class CategoriaAdmin(admin.ModelAdmin):
    
    list_display = ('id', 'nome', )


class PontoAdmin(admin.ModelAdmin):

    list_display = ('id', 'nome', 'latitude', 'longitude', )

    def render_change_form(self, request, context, *args, **kwargs):
        self.change_form_template = 'change_ponto.html'
        return super(PontoAdmin, self).render_change_form(request,
            context, *args, **kwargs)


admin.site.register(Categoria, CategoriaAdmin)
admin.site.register(Ponto, PontoAdmin)
