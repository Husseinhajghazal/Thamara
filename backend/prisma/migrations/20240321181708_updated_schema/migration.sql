/*
  Warnings:

  - A unique constraint covering the columns `[user_id]` on the table `Rate` will be added. If there are existing duplicate values, this will fail.

*/
-- CreateIndex
CREATE UNIQUE INDEX `Rate_user_id_key` ON `Rate`(`user_id`);
