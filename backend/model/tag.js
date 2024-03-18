const Sequelize = require("sequelize");
const sequelize = require("../util/database");

const Tag = sequelize.define("tag", {
  id: {
    type: Sequelize.INTEGER,
    autoIncrement: true,
    primaryKey: true,
    allowNull: false,
  },
  value: {
    type: Sequelize.STRING,
    allowNull: false,
  },
});

module.exports = Tag;
