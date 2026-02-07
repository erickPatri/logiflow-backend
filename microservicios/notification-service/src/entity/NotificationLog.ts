import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn } from "typeorm";

@Entity()
export class NotificationLog {
    @PrimaryGeneratedColumn()
    id!: number;

    @Column()
    orderId!: number;

    @Column()
    status!: string;

    @Column("text")
    message!: string; // aqui se guarda todo el JSON como texto para logs

    @CreateDateColumn()
    receivedAt!: Date;
}