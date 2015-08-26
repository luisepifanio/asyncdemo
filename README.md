# asyncdemo

La idea con este ejemplo es dar un puntapie inicial hacia la construcción de api
tomando en cuenta [algunas definiciones](http://farazdagi.com/blog/2014/rest-long-running-jobs/)

Si la idea es optimizar el uso de nuestras apis ( que a su vez hacen uso de otras apis, usando este clásico y 'prestado' ejemplo )
![llamadas múltiples  ](https://raw.githubusercontent.com/luisepifanio/asyncdemo/master/docs/imgs/multiple.api.calls.latency.png "sync vs async")

evitando latencias, timeouts y errores que se deriven de ello ( tengamos en cuenta que es deseable evitar 
operaciones bloqueantes, duraderas y de las que dependamos )
![llamadas múltiples  ](https://raw.githubusercontent.com/luisepifanio/asyncdemo/master/docs/imgs/multiple.async.api.png "sync vs async")
 
 considero que uno de los primeros pasos será hacer evolucionar nuestras apis para que sean más asíncronas ( si bien esto no soluciona el problema, ya que somos tan lentos como la más lenta de nuestras interfaces, nos permite ser más eficientes ) para luego poder
 encarar desafíos mayores ( async reactive design?? )

Graficamente se intento implementar una api **MUY SIMPLE** pero
que fuera asíncrona, utilizando una interfaz REST, y, de esta manera, evitar/reducir timeouts y errores
![comparación entre apis síncronas y asyncronas](https://raw.githubusercontent.com/luisepifanio/asyncdemo/master/docs/imgs/towards_an_async_api.png "sync vs async")
