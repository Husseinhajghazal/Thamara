import React from "react";
import LinkHead from "../../components/link/LinkHead";
import LinksList from "../../components/link/LinksList";

const page = () => {
  return (
    <div className="p-5 w-full">
      <LinkHead />
      <LinksList />
    </div>
  );
};

export default page;
