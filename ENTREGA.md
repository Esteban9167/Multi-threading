# Taller Multi-threading — Documentación de entrega

## Ejemplo propio: Cafetería con 3 meseros

### Caso de uso

En una cafetería hay **3 mesas** que deben ser atendidas. Cada mesa tarda un tiempo distinto:

| Mesa | Tiempo |
|------|--------|
| 1    | 3 s    |
| 2    | 2 s    |
| 3    | 4 s    |

**Sin multi-threading (secuencial):** un solo mesero atiende mesa 1, luego 2, luego 3.  
Tiempo total ≈ **9 segundos** (3+2+4).

**Con multi-threading (paralelo):** tres meseros (tres hilos) atienden las tres mesas a la vez.  
Tiempo total ≈ **4 segundos** (el de la mesa más lenta).

**Mejora:** menos espera para los clientes y mejor uso del personal cuando las tareas son independientes.

### Estrategia elegida

**ExecutorService + Callable + Future** (ver `EjemploPropio.java`).

| Estrategia | Por qué no en este caso |
|------------|-------------------------|
| Thread directo | Más código manual (start, join, recoger resultados). |
| Runnable | No devuelve valor; necesitamos un mensaje por mesa. |
| synchronized | Sirve para datos compartidos (contador); aquí cada mesa es independiente. |
| Callable + Future | Ideal: varias tareas en paralelo que **retornan** un resultado. |

---

## Cómo ejecutar todo

```bash
cd DYAS-Multi-threading
mvn compile

java -cp target/classes ThreadExample
java -cp target/classes RunnableExample
java -cp target/classes SynchronizedExample
java -cp target/classes ExecutorServiceExample
java -cp target/classes CallableExample
java -cp target/classes EjemploPropio
```

### Salida esperada (resumen)

| Clase | Qué debes ver |
|-------|----------------|
| ThreadExample | `Thread: 0` … `Thread: 4` (~5 s) |
| RunnableExample | `Runnable: 0` … `Runnable: 4` |
| SynchronizedExample | `Counter: 2000` |
| ExecutorServiceExample | `Task 1` y `Task 2` mezclados en consola |
| CallableExample | `Sum: 10` |
| EjemploPropio | Tres `[INICIO]`, tres `[FIN]`, tiempo total ~4 s |

### 3 errores comunes

1. **Usar `run()` en vez de `start()`** → no hay paralelismo real.  
   **Solución:** siempre `thread.start()` o el pool (`submit` / `invokeAll`).

2. **No hacer `shutdown()` del ExecutorService** → el programa puede no terminar bien.  
   **Solución:** `executor.shutdown()` en `finally`.

3. **Imprimir resultados antes de que acaben los hilos** → datos incorrectos.  
   **Solución:** `join()`, `invokeAll()` o `future.get()` antes del resumen.

### Cómo verificar paralelismo

- En `EjemploPropio`: tiempo total ~4 s, no ~9 s.
- Tres líneas `[INICIO]` antes del primer `[FIN]`.
- Nombres de hilo distintos: `pool-1-thread-1`, `-2`, `-3`.

---

## Presentación (~2 minutos)

> Hola. Mi ejemplo es una **cafetería con 3 meseros** que atienden 3 mesas al mismo tiempo.  
>  
> Si un solo mesero atiende en fila, tardan **9 segundos** en total. Con **3 hilos en paralelo**, tardan unos **4 segundos**, porque el tiempo total es el de la mesa más lenta, no la suma de todas.  
>  
> Usé **ExecutorService** con un pool de 3 hilos, **Callable** para que cada mesa devuelva un resultado, e **invokeAll** con **Future** para esperar y leer esos resultados. No usé solo Thread porque hay varias tareas similares y quiero resultados; Runnable no devuelve valor; synchronized no aplica porque no compartimos un contador entre mesas.  
>  
> Aprendí que el paralelismo ayuda cuando las tareas son **independientes**, y que hay que **esperar** a los hilos (`get`, `join`) antes de mostrar el resumen.  
>  
> **Pregunta para ustedes:** si dos mesas comparten **una sola caja registradora**, ¿basta con paralelizar o habría que usar `synchronized` en la caja? ¿Cómo cambiaría el tiempo?

---

## Paso 4 — Resumen personal (completa con tus palabras)

- Aprendí a crear hilos con **Thread** y **Runnable**.
- **synchronized** evita errores cuando varios hilos modifican el mismo dato.
- **ExecutorService** organiza muchas tareas con un pool de hilos.
- **Callable** y **Future** permiten obtener resultados de tareas asíncronas.
- El multi-threading mejora el tiempo cuando las tareas son independientes y hay recursos (CPU/hilos) disponibles.
