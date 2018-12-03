const FS = require('fs');

class Claim {
  constructor(id, left, top, width, height) {
    this.id = id;
    this.left = parseInt(left, 10);
    this.top = parseInt(top, 10);
    this.width = parseInt(width, 10);
    this.height = parseInt(height, 10);
    this.area = [];
  }

  static buildClaim(line) {
    const intermediate = line.split(' @ ');
    const part1 = intermediate[1].split(': ');
    const id = intermediate[0];
    const left = part1[0].split(',')[0];
    const top = part1[0].split(',')[1];
    const width = part1[1].split('x')[0];
    const height = part1[1].split('x')[1];

    return new Claim(id, left, top, width, height);
  }

  markArea() {
    for (let i = this.left; i < this.left + this.width; i += 1) {
      for (let j = this.top; j < this.top + this.height; j += 1) {
        // console.log({i, j});
        this.area.push([i, j]);
      }
    }
    return this;
  }
}

class Fabric {
  constructor() {
    const hash = {};
    for (let i = 0; i <= 999; i += 1) {
      for (let j = 0; j <= 999; j += 1) {
        // console.log({i, j});
        hash[[i, j]] = [];
      }
    }
    this.hash = hash;
  }

  claimArea(claim) {
    // console.log(claim.area);
    claim.area.forEach(area => this.hash[area].push(claim.id));
  }

  printOverbooked() {
    return Object.keys(this.hash).filter(k => this.hash[k].length >= 2).length;
  }
}

const readContents = () => {
  const rawContents = FS.readFileSync('resources/day3.txt');
  // console.log(rawContents.toString());
  return rawContents.toString();
};

const solve = () => {
  const contents = readContents().split('\n');
  contents.pop();
  const claims = contents
    .map(line => Claim.buildClaim(line))
    .map(claim => claim.markArea());

  const fabric = new Fabric();
  claims.forEach(claim => fabric.claimArea(claim));
  return fabric.printOverbooked();
};

module.exports = {
  readContents,
  solve,
};
