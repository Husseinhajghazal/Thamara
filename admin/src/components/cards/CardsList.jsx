"use client";

import React, { useEffect, useState } from "react";
import Card from "./Card";

const CardsList = () => {
  const [cards, setCards] = useState([]);

  useEffect(() => {
    fetch("https://one18-team.onrender.com/card", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setCards(data.cards.map((card) => ({ ...card, deck: card.deck.name })));
      });
  }, []);

  return (
    <div className="w-full my-5 grid grid-cols-5 gap-3">
      {cards.map((e) => (
        <Card
          key={e.id}
          question={e.question}
          id={e.id}
          answer_type={e.answer_type}
          deck_name={e.deck}
          deck_id={e.deck_id}
          wrongAnswer1={e.mc_choice_1}
          wrongAnswer2={e.mc_choice_2}
          wrongAnswer3={e.mc_choice_3}
          image_url={e.image_url}
          right_answer={
            e.answer_type == "tf"
              ? e.right_answer_tf
              : e.answer_type == "s"
              ? e.right_answer_s
              : e.right_answer_mc
          }
        />
      ))}
    </div>
  );
};

export default CardsList;
