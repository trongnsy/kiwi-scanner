const StringFormat = (str: string, ...args: string[]) =>
  str.replace(/{(\d+)}/g, (_, index) => args[index] || "");

export { StringFormat };
