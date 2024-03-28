"use client";

import React, { useEffect, useState } from "react";
import Collection from "./Collection";

const CollectionsList = () => {
  const [collections, setCollections] = useState([]);

  useEffect(() => {
    fetch("https://one18-team.onrender.com/collection", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setCollections(data.collections);
      });
  }, []);

  return (
    <div className="w-full my-5">
      {collections.map((e) => (
        <Collection key={e.id} name={e.name} id={e.id} />
      ))}
    </div>
  );
};

export default CollectionsList;
