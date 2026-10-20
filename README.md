Paolicias.Sweet - Aplicación Móvil de Banquetería y Pastelería Fina

¡Bienvenido al repositorio oficial de Paolicias.Sweet! Una aplicación móvil desarrollada en Android con Kotlin y Jetpack Compose, diseñada para la gestión de reservas, cotización escalonada y catálogo de servicios de banquetería y pastelería fina para eventos.


- Acerca del Proyecto -

Paolicias.Sweet es una solución móvil orientada a digitalizar la atención de clientes y la logística de reservas de banquetes. La aplicación permite a los usuarios explorar catálogos de productos, realizar solicitudes de eventos con restricciones alimentarias, calcular presupuestos automáticos con tarifas escalonadas y adjuntar comprobantes de pago para confirmar su fecha.

El desarrollo del proyecto fue diseñado de forma modular y escalable, siguiendo una estricta separación de responsabilidades entre componentes de interfaz reutilizables (ui/components) y pantallas completas (ui/screen).


- Características Principales -

Gestión de Usuarios:

Pantalla de Iniciar Sesión (LoginScreen).

Pantalla de Registro de Cuentas (RegistroScreen).


Catálogo de Servicios:

Exhibición de productos y paquetes de catering corporativo y pastelería fina (CatalogoScreen).

Tarjetas reutilizables (ProductoCard) con detalles, precios y llamadas a la acción.


Reserva y Logística de Eventos:

Formulario de captura de requerimientos de eventos (EventoScreen), incluyendo número de comensales, horarios y restricciones alimentarias (alergias, celíacos, vegetarianos).


Cotización Automática Escalonada:

Tarificador dinámico basado en rangos de comensales (CotizacionScreen).

Cálculo automático de abono del 50% y adicionales opcionales (ej. tortas personalizadas).


Validación de Pago:

Módulo para adjuntar y validar comprobantes de transferencia (ComprobanteScreen).


Navegación e Interfaz Consistente:

Modularity con Header y Footer estandarizados mediante Box y modificadores de dimensiones fijas.

Uso de Scaffold e innerPadding para un diseño limpio y moderno bajo Material Design 3.



- Tecnologías Utilizadas -

Lenguaje: Kotlin

UI Framework: Jetpack Compose

Diseño: Material Design 3

Entorno de Desarrollo: Android Studio
