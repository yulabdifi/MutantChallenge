Mutant Challenge

Proyecto Spring boot reactivo con arquitectura limpia.

Prerequisitos:
	Para ejecutar el programa, se requiere una versión de gradle de 5.6 o superior y JDK 11.
	
Pasos para ejecutar localmente:
1. Descargar este proyecto:
	git clone https://github.com/yulabdifi/MutantChallenge

2. Importar como proyecto gradle la aplicación en el IDE

3. Para la base de datos DynamoDB local, se hará uso de Docker de la siguiente manera:
	3.1. Descargar la imagen:
			docker pull amazon/dynamodb-local
	3.2. Correr dicha imagen (para este caso, se expone en el puerto 8000):
			docker run -p 8000:8000 amazon/dynamodb-local

4. Haciendo uso de AWS CLI (se puede descargar en https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html), se crea la tabla en la imagen de DynamoDB local, para nuestro caso el nombre de la tabla es MutantCheck. El endpoint-url que se especifica para la base de datos local se arma con la ip local y el puerto del punto anterior, es decir, http://localhost:8000. El partitionKey o id único será "dna" y no debe ser modificado, las capacidades de lectura y escritura pueden ser modificadas cómo se considere.
Una vez entendido lo anterior, ejecutamos por consola el siguiente comando:
	aws dynamodb create-table --table-name MutantCheck --attribute-definitions  AttributeName=dna,AttributeType=S  --key-schema  AttributeName=dna,KeyType=HASH --provisioned-throughput  ReadCapacityUnits=10,WriteCapacityUnits=5 --endpoint-url http://localhost:8000

5. Abrir el archivo application.yaml

6. Editar las variables dynamodb.endpoint con la ip local y puerto de la imagen docker de DynamoDB, es decir, http://localhost:8000. Y la variable dynamodb.mutant con el nombre de la tabla del punto anterior, en nuestro caso, MutantCheck, de la siguiente manera:
	dynamodb:
	  endpoint: http://localhost:8000
	  tbl-name:
	    mutant: MutantCheck
	
7. Correr la tarea bootRun de Gradle (gradle bootRun) y esperar hasta que se muestre en el log "Started MainApplication".