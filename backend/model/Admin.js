const Sequelize = require("sequelize");
const sequelize = require("../util/database");

const Admin = sequelize.define("admin", {
  id: {
    type: Sequelize.INTEGER,
    autoIncrement: true,
    primaryKey: true,
    allowNull: false,
  },
  first_name: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  last_name: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  email: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  password: { type: Sequelize.STRING, allowNull: false },
});

module.exports = Admin;
