import json

def categorize_event(event):
    """
    Categorize an event into one type:
    - date
    - friends
    - family
    - tourist
    
    Returns a single string category.
    """
    # Extract relevant data for analysis
    title = event.get('title', '').lower()
    description = event.get('description', '').lower()
    event_type = event.get('type', '').lower()
    cost = event.get('cost', '')
    
    combined_text = f"{title} {description} {event_type}"
    
    adult_keywords = ['sex', 'adult', '18+', 'mature', 'erotic', 'strip', 'nude', 
                      'nsfw', 'explicit', 'adult only']
    
    is_adult_content = any(kw in combined_text for kw in adult_keywords)
    
    date_keywords = [
        'romantic', 'couple', 'date night', 'candlelight', 'wine', 'intimate', 
        'romance', 'sunset', 'dinner', 'cocktail', 'rooftop', 'valentine', 
        'couples', 'love', 'two', 'duo', 'dating', 'spa date'
    ]
    
    friends_keywords = [
        'party', 'club', 'music', 'dj', 'festival', 'concert', 'beer', 'pub crawl',
        'comedy show', 'comedy', 'stand-up', 'group', 'gang', 'friends', 'squad',
        'nightlife', 'karaoke', 'games', 'gaming', 'pub', 'bar', 'night out',
        'squad goals', 'adventure', 'trek', 'hiking', 'team', 'pub quiz'
    ]
    
    family_keywords = [
        'family', 'kids', 'children', 'child', 'parent', 'parents', 'all ages',
        'exhibition', 'workshop', 'educational', 'museum', 'gallery', 'zoo',
        'park', 'picnic', 'fair', 'carnival', 'puppet show', 'animation',
        'cartoon', 'magic show', 'disney', 'family-friendly', 'families'
    ]
    
    tourist_keywords = [
    'tourist', 'landmark', 'historic site', 'heritage', 'monument', 'city tour',
    'guided tour', 'attraction', 'temple', 'cathedral', 'castle', 'sightseeing',
    'travel', 'must-see', 'tour', 'exploration', 'explore', 'visit', 'museum',
    'cultural site', 'historical', 'scenic', 'photo spot', 'iconic'
]

    category_scores = {
    'date': 0,
    'friends': 0,
    'family': 0,
    'tourist': 0
}

    
    if is_adult_content:
        category_scores['date'] += 3
        category_scores['friends'] += 2
        category_scores['family'] = -100

    if '18+' in combined_text or 'adults only' in combined_text or 'age limit - 18' in combined_text:
        category_scores['family'] = -100

    for keyword in date_keywords:
        if keyword in combined_text:
            category_scores['date'] += 1
            
    for keyword in friends_keywords:
        if keyword in combined_text:
            category_scores['friends'] += 1
            
    if not is_adult_content:
        for keyword in family_keywords:
            if keyword in combined_text:
                category_scores['family'] += 1
            
    for keyword in tourist_keywords:
       if keyword in combined_text:
           category_scores['tourist'] += 1


    if 'comedy' in event_type or 'stand-up' in combined_text:
        category_scores['friends'] += 2
        category_scores['date'] += 1
        if any(kw in combined_text for kw in ['adult humor', 'dirty jokes', 'sex jokes', 'explicit']):
            category_scores['family'] = -100

    if 'concert' in combined_text or 'music' in event_type:
        category_scores['friends'] += 2
        category_scores['date'] += 1

    if 'workshop' in combined_text or 'class' in combined_text:
        category_scores['tourist'] += 2

    if not is_adult_content and ('family' in combined_text or 'kids' in combined_text):
        category_scores['family'] += 3

    try:
        cost_value = int(cost) if cost else 0
        if cost_value > 1000:
            category_scores['date'] += 1
        elif cost_value > 500:
            category_scores['friends'] += 1
    except ValueError:
        pass
    
    section_id = event.get('section_id', '')
    
    if section_id == 'night':
        category_scores['date'] += 1
        category_scores['friends'] += 1
        if category_scores['family'] > -100:
            category_scores['family'] -= 1
    elif section_id in ['afternoon', 'morning']:
       if not is_adult_content:
           category_scores['family'] += 1
       category_scores['tourist'] += 1


    # Decide the single highest scoring category
    sorted_categories = sorted(
        category_scores.items(), 
        key=lambda x: x[1], 
        reverse=True
    )
    
    top_category = sorted_categories[0][0]  # Just pick the top one
    return top_category

def add_categories_to_events(events):
    """
    Add a single 'category' field to each event.
    """
    for event in events:
        event['category'] = categorize_event(event)
    
    return events

def save_categorized_events(events, filename='categorized_events.json'):
    """Save the categorized events to a JSON file."""
    try:
        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(events, f, ensure_ascii=False, indent=2)
        print(f"✅ Successfully saved {len(events)} categorized events to {filename}")
        return True
    except Exception as e:
        print(f"❌ Failed to save events to JSON: {e}")
        return False
