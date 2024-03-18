const Sequelize = require("sequelize");

const sequelize = new Sequelize("hackathon", "root", "Test1234", {
  dialect: "mysql",
  host: "localhost",
  logging: false,
});

module.exports = sequelize;
