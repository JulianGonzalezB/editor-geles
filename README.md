# editor-geles
Editor de geles
version: 1.0
Autor: Julian Gonzalez Betancur
Email: julian.gonzalezbetancur@ucr.ac.cr

Los geles de electroforesis son geles, normalmente de agarosa o poliacrilamida, de uso común en laboratorios de química, genética, microbiología, bioquímica y otras disciplinas relacionadas.

Estos geles consisten en una matriz de gel con pozos (Agujeros en una o varias filas), en los que se introducen las sustancias de interés que deben movilizarse a lo largo de la matriz en una dirección determinada según sus características con respecto al diseño del equipo electroforético.
Para más información: https://en.wikipedia.org/wiki/Gel_electrophoresis

Típicamente, los geles de electroforesis se utilizan con alta frecuencia y la información que proveen no es obvia a simple vista, por lo que resulta necesario llevar un control riguroso del contenido de cada pozo en cada gel para su posterior análisis detallado. Probablemente la mejor forma de almacenar dicha información es grabandola permanentemente en fotografías de cada gel, sin embargo, la edición de fotografías se puede tornar trabajosa y costar una enorme cantidad de tiempo si el número de muestras por gel es muy alta, si la información varía mucho entre geles y si se tienen muchos geles por día.

Este programa fue pensado para editar geles de electroforesis de cualquier tipo, permitiendo agregar, con esfuerzo y tiempo mínimo, información relevante como:

Etiquetas de cada uno de los pozos
Nombre del gel
Nombre o número del experimento Experimento
Protocolo utilizado
Fecha de corrida del gel

**USO**
*Gel:*


*Etiquetas e información:*

Para ingresar las etiquetas y toda la información extra que se quita colocar debe seguirse el formato que se ejemplifica a continuación. Esta información debe encontrarse en un archivo **.csv** con los valores separados por "**;**". El arvhico debe contener únicamente dos columnas hasta la versión 1.0; la primer columna debe corresponder, en las primeras 5 líneas, al tipo de información (Tipo de gel:, Proyecto:, Primer/Protocolo:, por ejemplo) y en la siguiente columna la información correspondiente como se ejemplifica en el formato de muestra.

Tipo de gel:	PCR
Proyecto:	2
Primer/Protocolo:	35
Fecha	12/10/1978
Observación	ninguna
poblacion	individuo
PV	1
PV	2
PV	3
PV	4
PV	5
PV	6
PV	7
PV	8
PV	9
PV	10

Las columnas de población e individuo se concatenan para etiquetar cada pozo en la forma "PV1" según el ejemplo anterior. El número de filas que deben venir en el archivo debe corresponder al número de pozos totales en el gel.

*Cargar la imagen .jpg:*
*Cargar el archivo .csv:*
*Características del gel:*
*Variables de formato:*
