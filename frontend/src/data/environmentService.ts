
const development: boolean = (!process.env.NODE_ENV || process.env.NODE_ENV === 'development');

export const EnvironmentService = {
    getEnvironment: () => development ? "development" : "production",
    getBaseUrl: () => development ? "http://localhost:8080" : "",
    getWSBaseUrl: () => development ? "http://localhost:8080/ws/downloads/" : "/ws/downloads/"
}
