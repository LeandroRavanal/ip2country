## Ejercicio de Programación

Construir una aplicación que dada una dirección IP, encuentre el país al que pertenece, y muestre:
* El nombre y código ISO del país.
* Los idiomas oficiales del país.
* Hora(s) actual(es) en el país (si el país cubre más de una zona horaria, mostrar todas).
* Distancia estimada entre Buenos Aires y el país, en km.
* Moneda local, y su cotización actual en dólares (si está disponible).

Basado en la información anterior, es necesario contar con un mecanismo para poder consultar las siguientes estadísticas de utilización del servicio con los siguientes agregados:
* Distancia más lejana a Buenos Aires desde la cual se haya consultado el servicio.
* Distancia más cercana a Buenos Aires desde la cual se haya consultado el servicio.
* Distancia promedio de todas las ejecuciones que se hayan hecho del servicio.

## Frameworks
En resumen es el paquete Spring Boot 2.1.5:
1. Spring Boot
2. Spring AOP        (aspectos infolog e reintentos)
4. Spring Cache      (info en memoria)
5. Spring Web        (puntos de acceso y consumo de servicios)
6. Spring Data Redis (info en memoria compartida)

## Endpoints

### TraceIp
POST /api/v1/traceip

Información de la IP suministrada. Se puede enviar por /index.html

### Summary
GET  /api/v1/summary

Resumen estadístico.

## Instalación / Ejecución

```
mvn clean package

docker network create lr-network

docker run --name redis --network lr-network -d redis
docker run --name redis_local -p 6379:6379 -d redis

docker run --name app1 --network lr-network -p 8080:8080 -e redis.hostname=redis -d lr/ip2country
docker run --name app2 --network lr-network -p 8081:8080 -e redis.hostname=redis -d lr/ip2country
docker run --name app3 --network lr-network -p 8082:8080 -e redis.hostname=redis -d lr/ip2country
docker run --name app4 --network lr-network -p 8083:8080 -e redis.hostname=redis -d lr/ip2country
docker run --name app5 --network lr-network -p 8084:8080 -e redis.hostname=redis -d lr/ip2country
```

## Funcionamiento

Cuando la aplicación está levantando se obtienen los paises y las cotizaciones. Se calcula la distancia a Argentina para cada país y se actualiza la cotización de las monedas.
Períodicamente se consultan las cotizaciones y se actualizan los valores.
Enviando un POST a /api/v1/traceip con la IP, se obtiene el país de esa IP y se adjunta a los otros datos previamente almacenados en memoria.
Se puede acceder a un resumen de información a traves de GET /api/v1/summary.

NOTA: Completar "currency.api.key" en "application.properties" con la key de "https://fixer.io/"
