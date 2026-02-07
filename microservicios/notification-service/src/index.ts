import amqp from 'amqplib';
import { DataSource } from 'typeorm';
import { NotificationLog } from './entity/NotificationLog';
import "reflect-metadata"; 
import express from 'express';     
import http from 'http';           
import { Server } from 'socket.io';

// --- CONFIGURACIÓN DEL SERVIDOR WEBSOCKET ---
const app = express();
const server = http.createServer(app);
const io = new Server(server, {
    cors: {
        origin: "*", // Permitimos que React (puerto 5173) se conecte
        methods: ["GET", "POST"]
    }
});

// Escuchamos conexiones de clientes (solo para debug)
io.on('connection', (socket) => {
    console.log('Nuevo cliente conectado al WebSocket:', socket.id);
    socket.on('disconnect', () => {
        console.log('Cliente desconectado:', socket.id);
    });
});

// --- CONFIGURACIÓN BASE DE DATOS ---
const AppDataSource = new DataSource({
    type: "postgres",
    host: process.env.DB_HOST || "localhost", 
    port: parseInt(process.env.DB_PORT || "5433"),
    username: process.env.DB_USER || "postgres",
    password: process.env.DB_PASSWORD || "admin",
    database: process.env.DB_NAME || "db_notifications",
    synchronize: true,
    logging: false,
    entities: [NotificationLog],
});

async function startService() {
try {
        await AppDataSource.initialize();
        console.log(`Base de datos conectada a ${process.env.DB_HOST || "localhost"}`);

        const logRepository = AppDataSource.getRepository(NotificationLog);

        // Leemos la URL completa de RabbitMQ de la variable de entorno
        const rabbitUrl = process.env.RABBITMQ_URL || 'amqp://guest:guest@localhost:5672';
        
        const connection = await amqp.connect(rabbitUrl);
        const channel = await connection.createChannel();
        
        const queue = 'notifications_queue';
        await channel.assertQueue(queue, { durable: true });

        console.log(`Notification Service escuchando en RabbitMQ: ${queue}`);

        channel.consume(queue, async (msg) => {
            if (msg !== null) {
                const contenido = msg.content.toString();
                const pedido = JSON.parse(contenido);

                console.log("Mensaje recibido de RabbitMQ:", pedido);

                // 1. GUARDAR EN BASE DE DATOS (Lo que ya hacías)
                try {
                    const log = new NotificationLog();
                    log.orderId = pedido.id;
                    log.status = pedido.status;
                    log.message = JSON.stringify(pedido);
                    await logRepository.save(log);
                    console.log(`Guardado en BD Log ID: ${log.id}`);
                } catch (error) {
                    console.error("Error guardando en BD:", error);
                }

                // 2. 🔥 EMITIR POR WEBSOCKET (La Magia)
                // Esto envía el mensaje a TODOS los Dashboards conectados
                io.emit('orders_update', pedido); 
                console.log("Notificación enviada por WebSocket a los clientes");

                channel.ack(msg); 
            }
        });

        // Iniciamos el servidor Socket.io en el puerto 3001
        server.listen(3001, () => {
            console.log('Servidor WebSocket corriendo en puerto 3001');
        });

    } catch (error) {
        console.error('Error iniciando el servicio:', error);
    }
}

startService();