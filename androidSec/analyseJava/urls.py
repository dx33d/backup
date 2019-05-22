#analyseJava/urls.py
from django.conf.urls import url
from analyseJava import views

urlpatterns = [
    url('$', views.HomePageView.as_view()),
]
