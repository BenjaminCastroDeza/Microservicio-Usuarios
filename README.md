# Microservicio 1: Auth & Usuarios (LevelUp Gamer)

Microservicio backend especializado en **Autenticación, Registro, Gestión de Roles y Emisión de Tokens JWT** para la plataforma **LevelUp Gamer** (Duoc UC - DSY1104 Evaluación Parcial 3).

---

## 🛠️ Tecnologías y Características
- **Puerto:** `8081`
- **Base de Datos:** MySQL (`levelup_auth_db`)
- **Seguridad:** Spring Security 6 Stateless + JJWT 0.12.6 (HMAC SHA-256)
- **Cifrado:** BCryptPasswordEncoder
- **Documentación:** Swagger OpenAPI 3 (`http://localhost:8081/swagger-ui.html`)
- **CORS:** Habilitado para comunicación con React (`localhost:5173`)

---

## 📡 Endpoints del Microservicio

### 1. Autenticación (`/api/auth`)
| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/api/auth/login` | Público | Valida correo y contraseña. Retorna JWT con claims de usuario y rol. |
| `POST` | `/api/auth/register` | Público | Registra nuevo cliente validando unicidad de correo y requisitos de contraseña. |
| `GET` | `/api/auth/me` | Autenticado | Retorna el perfil del usuario autenticado a partir del JWT. |
| `GET` | `/api/auth/validate` | Público/Headers | Valida la firma del token y retorna los claims asociados. |

### 2. Gestión de Usuarios (`/api/usuarios`)
| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/api/usuarios` | `ROLE_ADMIN` | Listado completo de usuarios registrados. |
| `GET` | `/api/usuarios/{id}` | `ROLE_ADMIN` | Información de un usuario específico. |
| `PATCH` | `/api/usuarios/{id}/rol` | `ROLE_ADMIN` | Cambia el rol de un usuario (`ROLE_ADMIN` o `ROLE_CLIENTE`). |

---

## 🔑 Usuarios Iniciales Preconfigurados (`DataInitializer`)
Al iniciar la aplicación por primera vez se cargan automáticamente:
- **Administrador:** `admin@levelup.cl` / `Admin123!` (Rol: `ROLE_ADMIN`)
- **Cliente:** `cliente@levelup.cl` / `User123!` (Rol: `ROLE_CLIENTE`)

---

## 🚀 Cómo Ejecutar

```powershell
cd "C:\Users\PC\Desktop\levelup-MSlogin\levelup-MSlogin"
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd spring-boot:run
```
Swagger UI disponible en: 👉 **[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)**
