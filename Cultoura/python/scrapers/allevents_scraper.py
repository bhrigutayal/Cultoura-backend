import requests
from bs4 import BeautifulSoup
from fake_useragent import UserAgent
import re
import html
import time
from datetime import datetime
import json

def get_event_details(url, headers):
    """Fetch and extract all details of an event from its page."""
    try:
        response = requests.get(url, headers=headers, timeout=10)
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # Initialize event details dictionary
        event_details = {
            'title': '',
            'description': '',
            'image_url': '',
            'duration': 1,
            'date': '',
            'cost': 0.0,
            'rating': '',
            'type': '',
            'section_id': '',  # Changed here
            'location': ''
        }
        
        # Extract title
        title_tag = soup.find('h1', class_='eps-heading-1')
        if title_tag:
            event_details['title'] = title_tag.get_text(strip=True)
        
        # Extract description
        desc_div = soup.find('div', class_='event-description-html')
        if desc_div:
            event_details['description'] = clean_description(desc_div.get_text(separator=' ', strip=True))
        
        # Extract image URL
        event_image = soup.find('img', class_='event-image')
        if event_image and 'src' in event_image.attrs:
            event_details['image_url'] = event_image['src']
        else:
            # Alternative method - look for image in meta tags
            meta_image = soup.find('meta', property='og:image')
            if meta_image and 'content' in meta_image.attrs:
                event_details['image_url'] = meta_image['content']
        
        # Extract date and time
        date_time = soup.find('p', class_='event-time-label')
        if date_time:
            date_text = date_time.get_text(strip=True)
            event_details['date'] = date_text
            
            # Calculate duration if start and end times are available
            try:
                # Check if there's a start and end time pattern
                time_pattern = r'(\d{1,2}:\d{2}\s*[ap]m)\s*-\s*(\d{1,2}:\d{2}\s*[ap]m)'
                time_match = re.search(time_pattern, date_text, re.IGNORECASE)
                
                if time_match:
                    start_time_str = time_match.group(1)
                    end_time_str = time_match.group(2)
                    # Convert start and end times to 24-hour format
                    start_time = datetime.strptime(start_time_str, '%I:%M %p')
                    end_time = datetime.strptime(end_time_str, '%I:%M %p')

                    # Calculate duration in hours
                    duration = int((end_time - start_time).total_seconds() / 3600)

                    # Store the duration in hours
                    event_details['duration'] = duration
                else:
                    # If no end time, extract just the time and set a default duration (e.g., 1 hour)
                    time_pattern = r'at\s+(\d{1,2}:\d{2}\s*[ap]m)'
                    time_match = re.search(time_pattern, date_text, re.IGNORECASE)
                    if time_match:
                       # You can set a default duration for single time events (like 1 hour)
                       event_details['duration'] = 1  # Default to 1 hour for a single time event
            except Exception as e:
                print(f"Error calculating duration: {e}")
        
        # Determine section_id based on time
        section_id = determine_section_id(event_details['date'])  # Changed here
        event_details['section_id'] = section_id  # Changed here
        
        # Extract cost/price
        price_info = soup.find('p', class_='body-text icon-text', string=lambda s: s and 'Starting at' in s)
        if price_info:
            price_text = price_info.get_text(strip=True)
            price_match = re.search(r'Starting at ([A-Z]{2,3})\s*(\d+)', price_text)
            if price_match:
                currency = price_match.group(1)
                amount = price_match.group(2)
                event_details['cost'] = float(amount)  # Just storing the numeric value
        
        # If no price found above, try the ticket info section
        if not event_details['cost']:
            ticket_rows = soup.select('table.ext_ticket_info tbody tr')
            if ticket_rows:
                try:
                    price_cell = ticket_rows[0].select_td[-1]
                    price_text = price_cell.get_text(strip=True)
                    price_match = re.search(r'(\d+)\s*([A-Z]{2,3})', price_text)
                    if price_match:
                        event_details['cost'] = float(price_match.group(1))
                except Exception:
                    pass
        
        # Extract location
        location_tag = soup.find('p', class_='event-location')
        if location_tag:
            event_details['location'] = location_tag.get_text(strip=True)
        
        # Extract event type/category
        event_tags = soup.select('.eps-event-tags')
        if event_tags:
            event_types = [tag.get_text(strip=True) for tag in event_tags]
            event_details['type'] = ', '.join(event_types)
        
        # Set default rating (since it's not available in the HTML)
        event_details['rating'] = '4.5'  # Default rating
        
        return event_details
    
    except Exception as e:
        print(f"❌ Error fetching event details: {e}")
        return None

def determine_section_id(date_string):
    """Determine the section ID based on the event time (morning, afternoon, night)."""
    try:
        morning_pattern = r'\b([5-9]|1[0-1])(:00|:\d{2})?\s*am\b'
        afternoon_pattern = r'\b(12|[1-5])(:00|:\d{2})?\s*pm\b'
        evening_pattern = r'\b([6-9]|1[0-1])(:00|:\d{2})?\s*pm\b'
        
        if re.search(morning_pattern, date_string, re.IGNORECASE):
            return 1
        elif re.search(afternoon_pattern, date_string, re.IGNORECASE):
            return 2
        elif re.search(evening_pattern, date_string, re.IGNORECASE):
            return 3
        else:
            return 2
    except Exception:
        return 2

def clean_description(raw_desc):
    """Clean up event description text."""
    if not raw_desc:
        return ""
    
    desc = html.unescape(raw_desc)
    desc = re.sub(r"[\u2018\u2019\u201c\u201d]", "'", desc)
    desc = re.sub(r"[\u2013\u2014]", "-", desc)
    desc = re.sub(r"\\n|\\r", "\n", desc)
    desc = re.sub(r"\s+", " ", desc)
    desc = re.sub(r"(\n)?•", r"\n•", desc)
    desc = re.sub(r"(?<!\n)\. ", ".\n", desc)
    desc = re.sub(r"Also check out other.*$", "", desc, flags=re.DOTALL)
    
    return desc.strip()

def scrape_allevents(city="delhi", max_pages=5):
    """Scrape events from allevents.in website with detailed information."""
    ua = UserAgent()
    headers = {'User-Agent': ua.random}
    events = []
    seen_links = set()
    
    for page in range(1, max_pages + 1):
        print(f"Scraping page {page}/{max_pages}...")
        url = f"https://allevents.in/{city}/all?page={page}"
        
        try:
            response = requests.get(url, headers=headers, timeout=10)
            soup = BeautifulSoup(response.text, 'html.parser')
            event_cards = soup.find_all('li', class_='event-card')
            
            if not event_cards:
                print(f"No events found on page {page}. Stopping.")
                break
            
            print(f"Found {len(event_cards)} event cards on page {page}")
            
            for card in event_cards:
                try:
                    link_tag = card.find('a', href=True)
                    if not link_tag:
                        continue
                    link = link_tag['href']
                    if not link.startswith('http'):
                        link = "https://allevents.in" + link
                    
                    if link in seen_links:
                        continue
                    seen_links.add(link)
                    
                    print(f"Fetching details for event: {link}")
                    
                    event_details = get_event_details(link, headers)
                    
                    if event_details:
                        events.append(event_details)
                    
                    time.sleep(1)
                    
                except Exception as e:
                    print(f"❌ Failed to process event: {e}")
            
            time.sleep(2)
            
        except Exception as e:
            print(f"❌ Failed to fetch page {page}: {e}")
    
    print(f"Total unique events collected: {len(events)}")
    return events

def scrape_insider(city="delhi", max_pages=1):
    """Scrape events from insider.in website with detailed information."""
    ua = UserAgent()
    headers = {'User-Agent': ua.random}
    events = []
    seen_links = set()
    
    base_url = f"https://insider.in/all-events-in-{city}"
    try:
        response = requests.get(base_url, headers=headers, timeout=10)
        soup = BeautifulSoup(response.text, 'html.parser')

        event_cards = soup.find_all('li', class_='card-list-item')
        print(f"Found {len(event_cards)} events on Insider.in")

        for card in event_cards:
            try:
                anchor_tag = card.find('a', href=True)
                if not anchor_tag:
                    continue
                link = "https://insider.in" + anchor_tag['href']
                
                if link in seen_links:
                    continue
                seen_links.add(link)
                
                event_details = {
                    'title': '',
                    'description': '',
                    'image_url': '',
                    'duration': 1,
                    'date': '',
                    'cost': 0.0,
                    'rating': ''
                }
                
                # You can fill it further as needed
                
            except Exception as e:
                print(f"❌ Failed to process event from Insider: {e}")

    except Exception as e:
        print(f"❌ Failed to fetch events from Insider: {e}")

    return events
