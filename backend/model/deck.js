const Sequelize = require("sequelize");
const sequelize = require("../util/database");

const Deck = sequelize.define("deck", {
  id: {
    type: Sequelize.INTEGER,
    autoIncrement: true,
    primaryKey: true,
    allowNull: false,
  },
  name: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  image_url: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  color: {
    type: Sequelize.STRING(7),
    allowNull: false,
  },
  level: {
    type: Sequelize.ENUM("سهل", "متوسط", "صعب"),
    allowNull: false,
  },
  ratesCount: { type: Sequelize.INTEGER, allowNull: false, defaultValue: 0 },
  rate: {
    type: Sequelize.INTEGER,
    allowNull: false,
    max: 5,
    min: 0,
  },
  version: { type: Sequelize.INTEGER, allowNull: false, defaultValue: 0 },
});

module.exports = Deck;
