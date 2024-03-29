import DecksList from "../../components/decks/DecksList";
import DeckHead from "../../components/decks/DeckHead";
import React from "react";

const page = () => {
  return (
    <div className="p-5 w-full">
      <DeckHead />
      <DecksList />
    </div>
  );
};

export default page;
