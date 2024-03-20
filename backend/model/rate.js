const Sequelize = require("sequelize");
const sequelize = require("../util/database");

const Rate = sequelize.define("rate", {
  id: {
    type: Sequelize.INTEGER,
    autoIncrement: true,
    primaryKey: true,
    allowNull: false,
  },
  deck_id: {
    type: Sequelize.INTEGER,
    allowNull: false,
  },
  user_id: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  value: {
    type: Sequelize.INTEGER,
    allowNull: false,
    max: 5,
    min: 0,
  },
});

module.exports = Rate;
