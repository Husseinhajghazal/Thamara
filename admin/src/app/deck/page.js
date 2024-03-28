import CollectionsList from "../../components/decks/CollectionsList";
import Head from "../../components/decks/DeckHead";
import React from "react";

const page = () => {
  return (
    <div className="p-5 w-full">
      <Head />
      <CollectionsList />
    </div>
  );
};

export default page;
