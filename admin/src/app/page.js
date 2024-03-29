import CollectionsList from "../components/collections/CollectionsList";
import ColHead from "../components/collections/ColHead";

export default function Home() {
  return (
    <div className="p-5 w-full">
      <ColHead />
      <CollectionsList />
    </div>
  );
}
