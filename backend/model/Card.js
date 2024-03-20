const Sequelize = require("sequelize");
const sequelize = require("../util/database");

const Card = sequelize.define("card", {
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
  quesition: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  image_url: {
    type: Sequelize.STRING,
    allowNull: false,
  },
  answer_type: {
    type: Sequelize.ENUM("mc", "tf", "s"),
    allowNull: false,
  },
  right_answer_tf: Sequelize.BOOLEAN,
  right_answer_mc: Sequelize.STRING,
  right_answer_s: Sequelize.STRING,
  mc_choice_1: Sequelize.STRING,
  mc_choice_2: Sequelize.STRING,
  mc_choice_3: Sequelize.STRING,
});

module.exports = Card;
