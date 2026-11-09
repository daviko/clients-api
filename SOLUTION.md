```markdown
# Solución — Examen Práctico

## Qué se implementó
Se implementó el nuevo endpoint DELETE /clientes/{id} (sugerido en el ejercicio) siguiendo el mismo estándar propuesto.

## Cómo se implementó
Se siguió el mismo modelo o arquitectura en capas (Controller, Service y Repository) con el fin de agregar la lógica para eliminar un
cliente por su id.

## Cambios en base de datos / configuración
Se mantiene la misma configuración y parámetros de la conexión a la base de datos propuesta SQL Server.

## Dificultades encontradas
1. El proyecto en Java no se pudo probar localmente debido a las limitaciones (técnicas, memoria, ram, procesador) del pc donde se realizó
2. Por el motivo anterior, se decidió abrir el proyecto en Visual Studio Code agregando la lógica necesaria para resolver el ejercicio
3. La idea de la estrategia anterior es mostrar la forma de trabajar y la lógica propuesta para resolver este ejercicio
4. El proyecto front en Angular tampoco se pudo probar y correr localmente debido al mismo motivo del punto 1

## Mejoras que podrían aplicarse
1. Si se tuviese mas tiempo y/o recursos, se podrían aplicar otros patrones de diseño con el fin de mejorar la calidad y la resiliencia de
la solución. Por ejemplo configurar:
- Circuit Breaker: Para el tema de resiliencia y tolerancia a fallos
- Builder: Agregar el patrón para la creación de objetos en la api y los tests unitarios.
2. De igual modo agregar JUnit tests (JUnit 5, Jupiter) con el fin de probar el correcto funcionamiento de cada unidad de código
3. Agregar lógica para mejorar la seguridad del aplicativo utilizando JWT en el backend y en el frontend
4. Cambiar la conexión a una base de datos H2 ya que es mas fácil de configurar en cualquier pc y se evita instalar otros motores (Oracle,
MySQL, PostgreSQL, SQL Server, etc)
```