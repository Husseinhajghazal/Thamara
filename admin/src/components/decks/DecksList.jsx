"use client";

import React, { useEffect, useState } from "react";
import Deck from "./Deck";

const CollectionsList = () => {
  const [decks, setDecks] = useState([]);

  useEffect(() => {
    fetch("https://one18-team.onrender.com/deck", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setDecks(
          data.decks.map((deck) => ({
            ...deck,
            collection_id: deck.collection.id,
            collection_name: deck.collection.name,
          }))
        );
      });
  }, []);

  return (
    <div className="w-full my-5 grid grid-cols-5 gap-3">
      {decks.map((e) => (
        <Deck
          key={e.id}
          name={e.name}
          id={e.id}
          color={e.color}
          level={e.level}
          allowShuffle={e.allowShuffle}
          col_id={e.collection_id}
          col_name={e.collection_name}
          cardsCount={e.cardsCount}
          rate={e.rate}
          ratesCounts={e.ratesCounts}
        />
      ))}
    </div>
  );
};

export default CollectionsList;
