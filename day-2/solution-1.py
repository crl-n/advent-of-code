with open("input") as file:
    lines = file.readlines()
    score = 0
    for line in lines:
        theirs, ours = line.strip().split()
        match theirs:
            case 'A':
                match ours:
                    case 'X':
                        score += 1 + 3
                    case 'Y':
                        score += 2 + 6
                    case 'Z':
                        score += 3 + 0
            case 'B':
                match ours:
                    case 'X':
                        score += 1 + 0
                    case 'Y':
                        score += 2 + 3
                    case 'Z':
                        score += 3 + 6
            case 'C':
                match ours:
                    case 'X':
                        score += 1 + 6
                    case 'Y':
                        score += 2 + 0
                    case 'Z':
                        score += 3 + 3
            
print("Score: ", score)
