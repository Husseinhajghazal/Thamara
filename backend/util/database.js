const Sequelize = require("sequelize");

const sequelize = new Sequelize(
  process.env.DATABASE,
  process.env.USER,
  process.env.PASSWORD,
  {
    dialect: "mysql",
    host: process.env.HOST,
    logging: false,
    define: {
      charset: "utf8",
      collate: "utf8_general_ci",
    },
  }
);

module.exports = sequelize;
