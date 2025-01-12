CREATE TABLE `respostas`(
`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`mensagem` TEXT NOT NULL,
`dataCriacao` DATETIME DEFAULT CURRENT_TIMESTAMP,
`solucao` TINYINT NOT NULL DEFAULT 0,
`topico_id` BIGINT NOT NULL,
`usuario_id` BIGINT NOT NULL,

CONSTRAINT `fk_respostas_usuario` FOREIGN KEY (usuario_id) REFERENCES `usuarios`(`id`),
CONSTRAINT `fk_topico` FOREIGN KEY (topico_id) REFERENCES `topicos`(`id`)

);